package controllers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import play.data.DynamicForm;

/**
 * Hilfsklasse um Multiselect-Listen auszulesen, da diese nicht standardisiert
 * sind und Play nicht richtig mit ihnen umgehen kann.
 * 
 * @author daniel
 *
 */
public class MultiselectList {

    /**
     * Utility Klasse braucht keinen public Konstruktor
     */
    private MultiselectList() {
    }

    /**
     * Diese Mehode gibt alle Multiselect-Values in Array-Form zurück. Als Name
     * muss der name-Tag des select-Elementes mitgegeben werden, jedoch ohne die
     * "[]" am Ende eines multiselect-Elementes.
     * 
     * @param form
     *            das Formular das die Daten enthält.
     * @param name
     *            der Name des select-Elementes.
     * @return ein Array mit Strings an selektierten Werten.
     */
    public static String[] getValueArray(DynamicForm form, String name) {
        Map<String, String> data = form.data();
        ArrayList<String> result = new ArrayList<>();
        for (Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().contains(name)) {
                result.add(entry.getValue());
            }
        }
        String[] array = new String[result.size()];
        return result.toArray(array);
    }
}
