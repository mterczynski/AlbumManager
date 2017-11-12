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

    public String toString(){
        return
            "{id: " + id +
            ", text: " + text +
            ", color: " + color +
            ", imagePath: " + imagePath +
            "}";
    }
}
