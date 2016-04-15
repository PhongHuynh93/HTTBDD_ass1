package dhbk.android.testgooglesearchreturn.ClassHelp;

/**
 * Created by Thien Nhan on 4/7/2016.
 */
public class RouteInfo {
    private String name;
    private String description;
    private String img;
    private String route;


    public RouteInfo() {
    }

    public RouteInfo(String name, String description, String img, String route) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
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
