package local.terczynski.albummanager.Helpers;

import android.graphics.Color;

public class Note {
    private String imagePath;
    private String title;
    private String text;
    private Color color;

    public Note(String ImagePath, String Title, String Text, Color Color) {
        this.imagePath = ImagePath;
        this.title = Title;
        this.text = Text;
        this.color = Color;
    }






    // getters and setters:

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
