package ddwu.mobile.finalproject.ma02_20180983;

import java.io.Serializable;

public class food implements Serializable {
    private long _id;
    private String name;
    private double volume;
    private double kcal;
    private double carbohydrate;
    private double protein;
    private double fat;
    private double sugar;
    private double natrium;
    private double cholesterol;
    private double fattyAcid;
    private double transfat;
    private String maker;
    private String photo;

    public food(){ }

    public food(long _id, String name, double volume, double kcal, double carbohydrate, double protein, double fat, double sugar, double natrium, double cholesterol, double fattyAcid, double transfat, String maker, String photo) {
        this._id = _id;
        this.name = name;
        this.volume = volume;
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.natrium = natrium;
        this.cholesterol = cholesterol;
        this.fattyAcid = fattyAcid;
        this.transfat = transfat;
        this.maker = maker;
        this.photo = photo;
    }

    public food(String name, double volume, double kcal, double carbohydrate, double protein, double fat, double sugar, double natrium, double cholesterol, double fattyAcid, double transfat, String maker, String photo) {
        this.name = name;
        this.volume = volume;
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.natrium = natrium;
        this.cholesterol = cholesterol;
        this.fattyAcid = fattyAcid;
        this.transfat = transfat;
        this.maker = maker;
        this.photo = photo;
    }

    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public double getKcal() { return kcal; }
    public void setKcal(double kcal) { this.kcal = kcal; }

    public double getCarbohydrate() { return carbohydrate; }
    public void setCarbohydrate(double carbohydrate) { this.carbohydrate = carbohydrate; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }

    public double getSugar() { return sugar; }
    public void setSugar(double sugar) { this.sugar = sugar; }

    public double getNatrium() { return natrium; }
    public void setNatrium(double natrium) { this.natrium = natrium; }

    public double getCholesterol() { return cholesterol; }
    public void setCholesterol(double cholesterol) { this.cholesterol = cholesterol; }

    public double getFattyAcid() { return fattyAcid; }
    public void setFattyAcid(double fattyAcid) { this.fattyAcid = fattyAcid; }

    public double getTransfat() { return transfat; }
    public void setTransfat(double transfat) { this.transfat = transfat; }

    public String getMaker() { return maker; }
    public void setMaker(String maker) { this.maker = maker; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}
