package bd.com.supply.module.user.domian;

public class FunItemBean {
    public static final int OPEN_SCAN=1;
    public static final int MOENY=2;
    public static final int CREATE_WALLET=3;
    private String name;
    private int type;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
