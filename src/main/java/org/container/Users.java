package org.container;

public enum Users {


    ADMIN1("Developer", "21101998"),
    ADMIN2("Hsu Wai Hnin", "21101998"),
    CUSTOMER1("Thu Thu Zin", "123"),
    CUSTOMER2("Ni Ni Win", "123"),
    GUEST("User", "1234");

    private final String username;
    private final String password;

    Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



}
