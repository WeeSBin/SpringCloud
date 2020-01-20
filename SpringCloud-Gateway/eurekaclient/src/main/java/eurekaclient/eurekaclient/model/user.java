package eurekaclient.eurekaclient.model;

import eurekaclient.item.model.item;

import java.util.List;

public class user {
    String name;
    String email;
    List<item> itemList;

    public user() {
    }

    public user(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public user(String name, String email, List<item> itemList) {
        this.name = name;
        this.email = email;
        this.itemList = itemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<item> getItemList() {
        return itemList;
    }

    public void setItemList(List<item> itemList) {
        this.itemList = itemList;
    }
}
