package local.terczynski.albummanager.Helpers;

import android.graphics.Color;

public class Note {
    public String imagePath;
    public String title;
    public String text;
    public int id;
    public String color;

    public Note(String Title, String Text, String Color, int id) {
        this.title = Title;
        this.text = Text;
        this.color = Color;
        this.id = id;
    }
    public Note(String Title, String Text, String Color, int id, String ImagePath) {
        this.imagePath = ImagePath;
        this.title = Title;
        this.text = Text;
        this.color = Color;
        this.id = id;
    }
    // getters and setters:

//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getImagePath() {
//        return imagePath;
//    }
//
//    public void setImagePath(String imagePath) {
//        this.imagePath = imagePath;
//    }
//
//    public Color getColor() {
//        return color;
//    }
//
//    public void setColor(Color color) {
//        this.color = color;
//    }
}
