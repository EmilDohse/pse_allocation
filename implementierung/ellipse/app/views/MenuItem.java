package views;

public class MenuItem {

    private String  menuText;
    private String  link;
    private boolean isActive = false;

    public MenuItem(String menuText, String link) {
        this.menuText = menuText;
        this.link = link;
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
