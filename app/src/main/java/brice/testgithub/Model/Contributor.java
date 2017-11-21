/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.Model;


public class Contributor {
    public final String login;
    public final int contributions;

    public Contributor(String login, int contributions) {
        this.login = login;
        this.contributions = contributions;
    }
}
