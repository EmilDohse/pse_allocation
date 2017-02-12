// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package importExport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import data.Achievement;
import data.Adviser;
import data.Allocation;
import data.GeneralData;
import data.Grade;
import data.LearningGroup;
import data.Project;
import data.Rating;
import data.SPO;
import data.Semester;
import data.Student;
import data.Team;
import exception.ImporterException;

/************************************************************/
/**
 * 
 */
public class Importer {

    private static final String WRONG_FORMAT      = "importer.wrongFileFormat";
    private static final String FILE_NOT_FOUND    = "importer.FileNotFound";
    private static final String IO                = "importer.IOException";
    private static final String MISSING_PROJECT   = "importer.missingProject";
    private static final String MISSING_STUDENT   = "importer.missingStudent";
    private static final String MISSING_SPO       = "importer.missingSPO";
    private static final String TEAMNR            = "importer.wrongTeamNumber";
    private static final String ALREADY_EXISTING  = "importer.alreadyExisting";
    private static final String NOTHING_TO_EXPORT = "admin.eximport.nothingToExport ";

    /**
     * Importiert eine Einteilung.
     * 
     * @param file
     *            Die Datei, aus der importiert wird..
     * @param semester
     *            Semester, dem die Einteilung hinzugefügt werden soll.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void importAllocation(File file, Semester semester)
            throws ImporterException {
        Allocation importedAllocation = new Allocation();
        ArrayList<Team> importedTeams = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String[] wantedHeader = { "Projekt", "Teamnummer", "Mitglieder" };
            String header = br.readLine();
            if (header == null) {
                throw new ImporterException(FILE_NOT_FOUND);
            }
            String[] headerSplit = header.split(";");

            // Prüfe ob Kopfzeile die korrekte Länge hat
            if (wantedHeader.length != headerSplit.length) {
                throw new ImporterException(WRONG_FORMAT);
            }

            // Prüfe, ob Kopfzeile die korrekten Inhalte hat
            for (int i = 0; i < wantedHeader.length; i++) {
                if (!wantedHeader[i].equals(headerSplit[i])) {
                    throw new ImporterException(WRONG_FORMAT);
                }
            }
            String line = new String();
            while ((line = br.readLine()) != null) {
                Team currentTeam = new Team();
                String[] lineSplit = line.split(";");

                // Prüfe, ob Zeile die korrekte Länge
                if (lineSplit.length != headerSplit.length) {
                    throw new ImporterException(WRONG_FORMAT);
                }

                // Prüfe, ob das Projekt existiert
                Project project = getProjectByName(semester, lineSplit[0]);
                if (project == null) {
                    throw new ImporterException(MISSING_PROJECT);
                } else {
                    currentTeam.setProject(project);
                }

                int teamNr;
                try {
                    teamNr = Integer.parseInt(lineSplit[1]);
                } catch (NumberFormatException e) {
                    throw new ImporterException(WRONG_FORMAT);
                }

                // Prüfe ob Teamnummer größer 0 und kleinergleich der
                // maximal
                // Anzahl Teams ist
                if (teamNr <= 0 || teamNr > project.getNumberOfTeams()) {
                    throw new ImporterException(TEAMNR);
                }

                // Prüfe, ob Teamnummer schon existiert
                if (duplicateTeamNumber(teamNr, importedTeams, project)) {
                    throw new ImporterException(TEAMNR);
                } else {
                    currentTeam.setTeamNumber(teamNr);
                }

                String[] studentsSplit = lineSplit[2].split(",");

                // Parse Matrikelnummer
                int[] matNrs = new int[studentsSplit.length];

                // Prüfe, ob es sich wirklich um Integers handelt
                for (int i = 0; i < matNrs.length; i++) {
                    try {
                        matNrs[i] = Integer.parseInt(studentsSplit[i]);
                    } catch (NumberFormatException e) {
                        throw new ImporterException(WRONG_FORMAT);
                    }
                }

                // Prüfe, ob die Studenten bereits existieren
                for (int i = 0; i < matNrs.length; i++) {
                    Student currentStudent = Student.getStudent(matNrs[i]);
                    if (currentStudent == null) {
                        throw new ImporterException(MISSING_STUDENT);
                    } else {
                        currentTeam.addMember(currentStudent);
                    }
                }

                currentTeam.setAllocation(importedAllocation);
                importedTeams.add(currentTeam);
                importedAllocation.doTransaction(() -> {
                    importedAllocation.setTeams(importedTeams);
                    importedAllocation.setName("importierte Einteilung");
                    importedAllocation.setParameters(new ArrayList<>());
                    importedAllocation.setSemester(
                            GeneralData.loadInstance().getCurrentSemester());
                });
            }
        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Exportiert eine Einteilung.
     * 
     * @param file
     *            Die Ausgabedatei.
     * @param allocation
     *            Die Einteilung, die exportiert werden soll.
     * @throws ImporterException
     *             wird vom Controller behandelt.
     */
    public void exportAllocation(File file, Allocation allocation)
            throws ImporterException {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {

            // Schreibe Kopfzeile
            String header = "Projekt;Teamnummer;Mitglieder";
            header = header.substring(0, header.length());
            bw.write(header);
            bw.newLine();

            // Erstelle Zeile pro Team und schreibe diese
            for (Team team : allocation.getTeams()) {
                String line = new String();
                line += team.getProject().getName() + ";";
                line += team.getTeamNumber() + ";";
                for (Student member : team.getMembers()) {
                    line += member.getMatriculationNumber() + ",";
                }
                line = line.substring(0, line.length() - 1);
                bw.write(line);
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Importiert zu PSE/TSE angemeldete Studenten.
     * 
     * @param file
     *            Pfad zu einer .csv Datei.
     * @param semester
     *            Das Semester, bei dem die Daten aktualisiert.
     */
    @Deprecated
    public void importCMSData(String file, Semester semester)
            throws ImporterException {
    }

    /**
     * Exportiert Noten von Studenten für das CMS.
     * 
     * @param file
     *            Der Ausgabepfad.
     * @param Das
     *            Semester, aus dem die Noten der Studenten exportiert werden
     *            sollen.
     */
    @Deprecated
    public void exportCMSData(String file, Semester semester)
            throws ImporterException {

    }

    /**
     * Importiert eine SPO.
     * 
     * @param file
     *            Pfad zu einer .csv Datei.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void importSPO(File file) throws ImporterException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            // Lese Kopfzeile
            String header = br.readLine();
            if (header == null) {
                throw new ImporterException(FILE_NOT_FOUND);
            }
            String[] headerSplit = header.split(";");

            // Prüfe, ob Kopzeile die richtige Länge, sowie richtige Namen hat
            boolean headerLength = (headerSplit.length == 3);
            boolean firstColumn = headerSplit[0].equals("Name");
            boolean secondColumn = headerSplit[1]
                    .equals("Additional Achievements");
            boolean thirdColumn = headerSplit[2]
                    .equals("Necessary Achievements");

            if (!(headerLength && firstColumn && secondColumn && thirdColumn)) {
                throw new ImporterException(WRONG_FORMAT);
            }

            // Lese die Zeile, in der die SPO steht
            String line = br.readLine();

            // Teile die Zeile in Attribute auf
            String[] lineSplit = line.split(";");

            // Prüfe, ob die Zeile die korrekte Form hat und ob die SPO noch
            // nicht existiert
            if (lineSplit.length != 3) {
                throw new ImporterException(WRONG_FORMAT);
            }

            SPO spo = SPO.getSPO(lineSplit[0]);
            if (spo != null) {
                throw new ImporterException(ALREADY_EXISTING);
            }

            boolean additionalIsNotEmpty = (lineSplit[1].length() != 0);
            boolean necessaryIsNotEmpty = (lineSplit[2].length() != 0);

            // Prüfe, ob die zu importierende SPO notwendige und zusätzliche
            // Teilleistungen enthält
            if (!(additionalIsNotEmpty && necessaryIsNotEmpty)) {
                throw new ImporterException(WRONG_FORMAT);
            }

            // Erstelle die neue SPO
            SPO importedSpo;
            importedSpo = new SPO();
            importedSpo.doTransaction(() -> {
                importedSpo.setName(lineSplit[0]);
            });

            List<Achievement> additionalAchievements = new ArrayList<Achievement>();
            List<Achievement> necessaryAchievements = new ArrayList<Achievement>();
            // Teile die zusätzlichen Teilleistungen weiter auf
            String[] additionalSplit = lineSplit[1].split(",");
            for (int i = 0; i < additionalSplit.length; i++) {

                // Prüfe, ob die aktuelle Teilleistung schon
                // existiert, wenn nicht lege sie an
                Achievement currentAchievement = Achievement
                        .getAchievement(additionalSplit[i]);
                if (currentAchievement != null) {
                    additionalAchievements.add(currentAchievement);
                } else {

                    Achievement newAchievement = new Achievement();
                    String name = additionalSplit[i];
                    newAchievement.doTransaction(() -> {
                        newAchievement.setName(name);
                    });
                    additionalAchievements.add(newAchievement);

                }
            }

            // Selbes Vorgehen für notwendige Teilleistungen
            String[] necessarySplit = lineSplit[2].split(",");
            for (int i = 0; i < necessarySplit.length; i++) {
                Achievement currentAchievement = Achievement
                        .getAchievement(necessarySplit[i]);
                if (currentAchievement != null) {
                    necessaryAchievements.add(currentAchievement);
                } else {
                    Achievement newAchievement;

                    newAchievement = new Achievement();
                    String name = necessarySplit[i];
                    newAchievement.doTransaction(() -> {
                        newAchievement.setName(name);
                    });
                    necessaryAchievements.add(newAchievement);

                }
            }
            importedSpo.doTransaction(() -> {
                importedSpo.setAdditionalAchievements(additionalAchievements);
                importedSpo.setNecessaryAchievements(necessaryAchievements);
            });
        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);

        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Exportiert eine SPO.
     * 
     * @param file
     *            Der Ausgabepfad.
     * @param spo
     *            Die SPO, die exportiert werden soll.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void exportSPO(File file, SPO spo) throws ImporterException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {

            // Write Header
            writer.write("Name;Additional Achievements;Necessary Achievements");
            writer.newLine();

            // Create output String
            String output = spo.getName();
            output += ";";

            // Add all additional Achievements
            for (Achievement achievement : spo.getAdditionalAchievements()) {
                output += achievement.getName() + ",";
            }

            // Remove trailing comma
            output = output.substring(0, output.length() - 1) + ";";

            // Add all necessary Achievements
            for (Achievement achievement : spo.getNecessaryAchievements()) {
                output += achievement.getName() + ",";
            }

            // remove trailing comma
            output = output.substring(0, output.length() - 1);

            // write line
            writer.write(output);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }

    }

    /**
     * Importiert eine Liste von Studenten aus einer .csv Datei. Studenten
     * werden nur importiert, wenn noch keine Studenten für das Semester
     * registriert sind. Studenten werden nur importiert, wenn alle Projekte
     * existieren. Importierte Studenten haben keine Noten vergeben, sind nicht
     * für TSE und PSE angemeldet und ihre E-Mail ist nicht verifiziert.
     * 
     * @param file
     *            Pfad zu einer .csv-Datei.
     * @param semester
     *            Das Semester, dem die Studenten hinzugefügt werden sollen.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void importStudents(File file, Semester semester)
            throws ImporterException {
        // Prüfe ob die Studentenliste leer ist
        if (!(semester.getStudents().size() == 0)) {
            throw new ImporterException("importer.notEmptyStudents");
        }
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<LearningGroup> learningGroups = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            int wantedColumns = 11 + semester.getProjects().size();
            String[] wantedHeader = { "MatNr", "Vorname", "Nachname", "E-Mail",
                    "Passwort", "Lerngruppenname", "LerngruppePasswort", "SPO",
                    "Fachsemester", "Bestandene Teilleistungen",
                    "Noch ausstehende Teilleistungen" };

            String header = br.readLine();
            if (header == null) {
                throw new ImporterException(FILE_NOT_FOUND);
            }
            String[] headerSplit = header.split(";");

            // Prüfe ob Header die korrekte Spaltenanzahl hat
            if (!(headerSplit.length == wantedColumns)) {
                throw new ImporterException(WRONG_FORMAT);
            }

            // Prüfe ob Header korrekte Namen hat
            boolean correctHeader = true;
            for (int i = 0; i < wantedHeader.length; i++) {
                if (!wantedHeader[i].equals(headerSplit[i])) {
                    correctHeader = false;
                }
            }
            if (!correctHeader) {
                throw new ImporterException(WRONG_FORMAT);
            }

            // Prüfe, ob alle Projekte schon im Semester existieren
            boolean projectsExist = true;
            for (int i = wantedHeader.length; i < headerSplit.length; i++) {
                String currentProjectName = headerSplit[i];
                if (getProjectByName(semester, currentProjectName) == null) {
                    projectsExist = false;
                }
            }
            if (!projectsExist) {
                throw new ImporterException(MISSING_PROJECT);
            }

            // Ab hier beginnt das eigentliche Importieren
            String line = new String();
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(";");
                if (lineSplit.length != wantedColumns) {
                    throw new ImporterException(WRONG_FORMAT);
                }
                // Prüfe, ob zulässige Eingabe gemacht worden
                // sind
                int matNr;
                int semesterNumber;
                int[] ratings = new int[semester.getProjects().size()];
                try {
                    matNr = Integer.parseInt(lineSplit[0]);
                    semesterNumber = Integer.parseInt(lineSplit[8]);
                    for (int i = 0; i < ratings.length; i++) {
                        ratings[i] = Integer.parseInt(lineSplit[11 + i]);
                    }
                } catch (NumberFormatException e) {
                    throw new ImporterException(WRONG_FORMAT);
                }

                // Prüfe, ob der Student schon im der Datenbank existiert
                if (Student.getStudent(matNr) != null) {
                    throw new ImporterException(ALREADY_EXISTING);
                }

                // Prüfe, ob die angegebene SPO existiert
                SPO spo = SPO.getSPO(lineSplit[7]);
                if (spo == null) {
                    throw new ImporterException(MISSING_SPO);
                }

                ArrayList<Achievement> completedAchievements = new ArrayList<>();
                ArrayList<Achievement> oralTestAchievements = new ArrayList<>();

                // Füge die Teilleistungen den Listen hinzu
                String[] completedSplit = lineSplit[9].split(",");
                completedAchievements = parseAchievements(completedSplit, spo);
                if (lineSplit[10].length() != 0) {
                    String[] oralSplit = lineSplit[10].split(",");
                    oralTestAchievements = parseAchievements(oralSplit, spo);
                }

                // Prüfe, ob alle notwendigen Teilleistungen bestanden
                // wurden
                for (int i = 0; i < spo.getNecessaryAchievements()
                        .size(); i++) {
                    if (!completedAchievements
                            .contains(spo.getNecessaryAchievements().get(i))) {
                        throw new ImporterException(WRONG_FORMAT);
                    }
                }
                String firstName = lineSplit[1];
                String lastName = lineSplit[2];
                String email = lineSplit[3];
                String password = lineSplit[4];
                // Erzeuge den Studenten
                Student importedStudent = new Student();
                importedStudent.setCompletedAchievements(completedAchievements);
                importedStudent.setOralTestAchievements(oralTestAchievements);
                // TODO Die beiden oben können aus irgendnem Grund nicht ins
                // Lambda
                importedStudent.doTransaction(() -> {
                    importedStudent.setUserName(new String() + matNr);
                    importedStudent.setPassword(password);
                    importedStudent.setFirstName(firstName);
                    importedStudent.setLastName(lastName);
                    importedStudent.setEmailAddress(email);
                    importedStudent.setMatriculationNumber(matNr);
                    importedStudent.setSPO(spo);
                    importedStudent.setSemester(semesterNumber);
                    importedStudent.setRegisteredPSE(false);
                    importedStudent.setRegisteredTSE(false);
                    importedStudent.setIsEmailVerified(false);
                    importedStudent.setGradePSE(Grade.UNKNOWN);
                    importedStudent.setGradeTSE(Grade.UNKNOWN);
                });
                students.add(importedStudent);
                semester.doTransaction(() -> {
                    semester.setStudents(students);
                });
                LearningGroup currentGroup;
                if (lineSplit[5].length() != 0) {
                    // Erzeuge neue Gruppe mit dem gesetzten Namen, falls
                    // sie
                    // noch nicht existiert
                    if (LearningGroup.getLearningGroup(lineSplit[5],
                            semester) == null) {
                        currentGroup = new LearningGroup();
                        currentGroup.doTransaction(() -> {
                            currentGroup.setName(lineSplit[5]);
                            currentGroup.addMember(importedStudent);
                            currentGroup.setPrivate(false);
                            currentGroup.setPassword(lineSplit[6]);
                        });
                        learningGroups.add(currentGroup);
                        // Füge der Gruppe mit dem gesetzten Namen den
                        // Studenten
                        // hinzu, falls sie schon existiert
                    } else {
                        currentGroup = LearningGroup
                                .getLearningGroup(lineSplit[5], semester);
                        currentGroup.doTransaction(() -> {
                            currentGroup.addMember(importedStudent);
                        });
                    }
                    // Erstelle eine private Lerngruppe, wenn keine
                    // angegeben
                    // wurde
                } else {
                    currentGroup = new LearningGroup();
                    currentGroup.doTransaction(() -> {
                        currentGroup.setName(new String()
                                + importedStudent.getMatriculationNumber());
                        currentGroup.setPrivate(true);
                        currentGroup.addMember(importedStudent);
                        currentGroup.setPassword(new String());
                    });
                    learningGroups.add(currentGroup);
                }
                ArrayList<Rating> LeaningGoupRatings = new ArrayList<>();
                Rating currentRating;
                for (int i = 11; i < lineSplit.length; i++) {
                    currentRating = new Rating(ratings[i - 11],
                            getProjectByName(semester, headerSplit[i]));
                    currentRating.save();
                    LeaningGoupRatings.add(currentRating);
                }
                currentGroup.doTransaction(() -> {
                    currentGroup.setRatings(LeaningGoupRatings);
                    semester.setLearningGroups(learningGroups);
                });
            }
        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Exportiert Liste aller registrierten Studenten in einem Semester.
     * 
     * @param file
     *            Die Ausgabedatei.
     * @param semester
     *            Das Semester, dessen Studenten exportiert werden sollen.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void exportStudents(File file, Semester semester)
            throws ImporterException {
        if (semester.getStudents().isEmpty()) {
            throw new ImporterException(NOTHING_TO_EXPORT);
        }
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
            String header = "MatNr;Vorname;Nachname;E-Mail;Passwort;Lerngruppenname;LerngruppePasswort;"
                    + "SPO;Fachsemester;Bestandene Teilleistungen;Noch ausstehende Teilleistungen;";
            for (Project project : semester.getProjects()) {
                header += project.getName() + ";";
            }
            header = header.substring(0, header.length() - 1);

            bw.write(header);
            bw.newLine();

            for (Student student : semester.getStudents()) {
                String output = new String();
                output += student.getMatriculationNumber() + ";";
                output += student.getFirstName() + ";";
                output += student.getLastName() + ";";
                output += student.getEmailAddress() + ";";
                output += student.getPassword() + ";";
                LearningGroup lg = semester.getLearningGroupOf(student);
                if (!lg.isPrivate()) {
                    output += lg.getName() + ";";
                    output += lg.getPassword() + ";";
                } else {
                    output += ";;";
                }

                output += student.getSPO().getName() + ";";
                output += student.getSemester() + ";";

                for (Achievement achievement : student
                        .getCompletedAchievements()) {
                    output += achievement.getName() + ",";
                }
                output = output.substring(0, output.length() - 1) + ";";

                if (!student.getOralTestAchievements().isEmpty()) {
                    for (Achievement achievement : student
                            .getOralTestAchievements()) {
                        output += achievement.getName() + ",";
                    }
                    output = output.substring(0, output.length() - 1) + ";";
                } else {
                    output += ";";
                }

                for (Project project : semester.getProjects()) {
                    int rating;
                    rating = semester.getLearningGroupOf(student)
                            .getRating(project);
                    output += rating + ";";
                }
                output.substring(0, output.length() - 1);
                bw.write(output);
                bw.newLine();
            }

        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Importiert eine Liste von Projekten.
     * 
     * @param file
     *            Der Ausgabepfad.
     * @param Semester
     *            Das Semester, dem die Projekte hinzugefügt werden sollen.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void importProjects(File file, Semester semester)
            throws ImporterException {

        // Prüfe, ob die Projektliste des Smeesters noch leer ist
        if (!semester.getProjects().isEmpty()) {
            throw new ImporterException("importer.notEmptyProjects");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String header = br.readLine();
            if (header == null) {
                throw new ImporterException(FILE_NOT_FOUND);
            }
            String[] wantedHeader = { "Name", "Institut", "Anzahl Teams",
                    "Min. Size", "Max. Size", "Projekt URL", "Projektinfo" };
            String[] headerSplit = header.split(";");

            // Prüfe, ob Header die gewünschte Länge hat
            if (headerSplit.length != wantedHeader.length) {
                throw new ImporterException(WRONG_FORMAT);
            }

            // Prüfe, ob Header die richtigen Spalten enthält
            for (int i = 1; i < headerSplit.length; i++) {
                if (!(headerSplit[i].equals(wantedHeader[i]))) {
                    throw new ImporterException(WRONG_FORMAT);
                }
            }
            String line = new String();
            ArrayList<Project> projects = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(";");
                if (lineSplit.length != wantedHeader.length) {
                    throw new ImporterException(WRONG_FORMAT);
                }

                // Erzeuge die einzelnen Attribute
                String name = lineSplit[0];
                String institute = lineSplit[1];
                String url = lineSplit[5];
                String info = lineSplit[6];
                int numberOfTeams;
                int minSize;
                int maxSize;
                try {
                    numberOfTeams = Integer.parseInt(lineSplit[2]);
                    minSize = Integer.parseInt(lineSplit[3]);
                    maxSize = Integer.parseInt(lineSplit[4]);
                } catch (NumberFormatException e) {
                    throw new ImporterException(WRONG_FORMAT);
                }

                // Prüfe ob die Zahlenwerte sinnvoll sind
                if (numberOfTeams < 1 || minSize > maxSize) {
                    throw new ImporterException(WRONG_FORMAT);
                }
                if (minSize < -1) {
                    throw new ImporterException(WRONG_FORMAT);
                }
                if (!(maxSize > 0 || maxSize == -1)) {
                    throw new ImporterException(WRONG_FORMAT);
                }
                Project importedProject;
                importedProject = new Project();
                importedProject.doTransaction(() -> {
                    importedProject.setName(name);
                    importedProject.setMinTeamSize(minSize);
                    importedProject.setMaxTeamSize(maxSize);
                    importedProject.setNumberOfTeams(numberOfTeams);
                    importedProject.setProjectInfo(info);
                    importedProject.setProjectURL(url);
                    importedProject.setInstitute(institute);
                    importedProject.setAdvisers(new ArrayList<Adviser>());
                });
                projects.add(importedProject);
            }
            semester.doTransaction(() -> {
                semester.setProjects(projects);
            });

        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Exportiert die Projekte eines Semesters.
     * 
     * @param file
     *            Der Ausgabepfad.
     * @param semester
     *            Das Semester, aus dem die Projekte exportiert werden sollen.
     * @throws ImporterException
     *             wird vom Controller behandelt.
     */
    public void exportProjects(File file, Semester semester)
            throws ImporterException {
        if (semester.getProjects().isEmpty()) {
            throw new ImporterException(NOTHING_TO_EXPORT);
        }
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
            String header = "Name;Institut;Anzahl Teams;Min. Size;Max. Size;Projekt URL;Projektinfo";
            bw.write(header);
            bw.newLine();

            for (Project project : semester.getProjects()) {
                String line = new String();
                line += project.getName() + ";";
                line += project.getInstitute() + ";";
                line += project.getNumberOfTeams() + ";";
                line += project.getMinTeamSize() + ";";
                line += project.getMaxTeamSize() + ";";
                line += project.getProjectURL() + ";";
                line += project.getProjectInfo();

                bw.write(line);
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    /**
     * Exportiert die Noten von PSE/TSE eines Semesters.
     * 
     * @param file
     *            Die Ausgabedatei.
     * @param semester
     *            Das Semester, aus dem exportiert werden soll.
     * @throws ImporterException
     *             Wird vom Controller behandelt.
     */
    public void exportGrades(File file, Semester semester)
            throws ImporterException {
        if (semester.getStudents().isEmpty()) {
            throw new ImporterException(NOTHING_TO_EXPORT);
        }
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
            String header = "Matrikelnummer;Note PSE;Note TSE";
            bw.write(header);
            bw.newLine();
            for (Student student : semester.getStudents()) {
                String line = new String();
                line += student.getMatriculationNumber() + ";";
                line += student.getGradePSE().getName() + ";";
                line += student.getGradeTSE().getName();
                bw.write(line);
                bw.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new ImporterException(FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new ImporterException(IO);
        }
    }

    private ArrayList<Achievement> parseAchievements(String[] split, SPO spo)
            throws ImporterException {
        ArrayList<Achievement> achievements = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            Achievement currentAchievement = Achievement
                    .getAchievement(split[i]);
            if (currentAchievement == null) {
                throw new ImporterException(WRONG_FORMAT);
            }
            // Prüfe, ob die Teilleistungen in der SPO existieren
            if (!((spo.getNecessaryAchievements().contains(currentAchievement))
                    || spo.getAdditionalAchievements()
                            .contains(currentAchievement))) {
                throw new ImporterException(WRONG_FORMAT);
            }
            achievements.add(currentAchievement);
        }

        return achievements;
    }

    private Project getProjectByName(Semester semester, String name) {
        Project project = null;
        for (int i = 0; i < semester.getProjects().size(); i++) {
            if (semester.getProjects().get(i).getName().equals(name)) {
                project = semester.getProjects().get(i);
            }
        }
        return project;
    }

    private boolean duplicateTeamNumber(int teamNr, ArrayList<Team> teams,
            Project project) {
        List<Team> wantedTeams = teams.stream()
                .filter(team -> team.getProject().equals(project))
                .collect(Collectors.toList());
        boolean duplicate = false;
        for (int i = 0; i < wantedTeams.size(); i++) {
            if (teamNr == wantedTeams.get(i).getTeamNumber()) {
                duplicate = true;
            }
        }
        return duplicate;
    }
}
