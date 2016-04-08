package dhbk.android.testgooglesearchreturn.ClassHelp;

/**
 * Created by Thien Nhan on 4/7/2016.
 */
public class RouteInfo {
    public String name;
    public String description;
    public String img;

    public RouteInfo() {

    }

    public RouteInfo(String name, String description, String img) {
        this.name = name;
        this.description = description;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
