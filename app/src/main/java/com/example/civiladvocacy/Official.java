package com.example.civiladvocacy;

import java.util.ArrayList;

public class Official {
    private String position;
    private String name;
    private String party;
    private String phone;
    private String email;
    private String address;
    private String website;
    private String photoURL;
    private String [] official_social_links = new String [3];

    public Official (String po, String n, String pa, String ph, String em, String addr, String web, String photo, String [] social){
        setPosition(po);
        setName(n);
        setParty(pa);
        setPhone(ph);
        setEmail(em);
        setAddress(addr);
        setWebsite(web);
        setPhotoURL(photo);
        official_social_links = social;
    }

    //setter
    public void setPosition(String position) {
        this.position = position;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(String party) {
        this.party = "(" + party + ")";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    //getter
    public String getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getWebsite() {
        return website;
    }

    public String [] getSocialLink(){
        return official_social_links;
    }

    //toString just in case

    @Override
    public String toString() {
        return "Official{" +
                "position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                '}';
    }
}
