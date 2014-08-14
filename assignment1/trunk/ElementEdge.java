
public class ElementEdge{

    /**  */
    private City element;
    /**  */
    private int count = 1;
        
    /**
    * CONSTRUCTOR
    *
    */
    public ElementEdge(City c) {
        element = c;
    }


    /***********************************
    ******* GETTERS AND SETTERS ********
    ***********************************/

    public City get_city() {
        return element;
    }

    public int get_count() {
        return count;
    }

    public void count_up() {
        count++;
    }

    public void count_down() {
        count--;
    }
}