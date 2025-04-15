package models;



public class Bike {
    String name;
    String price;
    String releaseDate;

    public Bike(String name, String price, String releaseDate) {
        this.name = name;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Bike{name='" + name + "', price='" + price + "', releaseDate='" + releaseDate + "'}";
    }
    

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public String getPrice() {
		// TODO Auto-generated method stub
		return price;
	}
}


