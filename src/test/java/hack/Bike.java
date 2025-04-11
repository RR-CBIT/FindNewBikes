package hack;



class Bike {
    String name;
    String price;
    String releaseDate;

    Bike(String name, String price, String releaseDate) {
        this.name = name;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Bike{name='" + name + "', price='" + price + "', releaseDate='" + releaseDate + "'}";
    }
    
    double getNumericPrice() {
        String numeric = price.replace("Rs.", "").trim();
        if (numeric.contains("Lakh")) {
            return Double.parseDouble(numeric.replace("Lakh", "").trim()) * 100000;
        } else if (numeric.contains(",")) {
            return Double.parseDouble(numeric.replace(",", "").trim());
        } else {
            return Double.parseDouble(numeric.trim());
        }
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


