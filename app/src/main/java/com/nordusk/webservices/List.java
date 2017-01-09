package com.nordusk.webservices;

/**
 * Created by NeeloyG on 11/20/2016.
 */
public class List
{
    private String created_by;

    private String anniversary;

    private String territory;

    private String alternative_mobile;

    private String type;

    private String id;

    private String updated_at;

    private String address;

    private String email;

    private String name;

    private String dob;

    private String created_at;

    private String longitude;

    private String latitude;

    private String mobile;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParrent_id() {
        return parrent_id;
    }

    public void setParrent_id(String parrent_id) {
        this.parrent_id = parrent_id;
    }

    private  String parrent_id;
    public String getCreated_by ()
    {
        return created_by;
    }

    public void setCreated_by (String created_by)
    {
        this.created_by = created_by;
    }

    public String getAnniversary ()
    {
        return anniversary;
    }

    public void setAnniversary (String anniversary)
    {
        this.anniversary = anniversary;
    }

    public String getTerritory ()
    {
        return territory;
    }

    public void setTerritory (String territory)
    {
        this.territory = territory;
    }

    public String getAlternative_mobile ()
    {
        return alternative_mobile;
    }

    public void setAlternative_mobile (String alternative_mobile)
    {
        this.alternative_mobile = alternative_mobile;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [created_by = "+created_by+", anniversary = "+anniversary+", territory = "+territory+", alternative_mobile = "+alternative_mobile+", type = "+type+", id = "+id+", updated_at = "+updated_at+", address = "+address+", email = "+email+", name = "+name+", dob = "+dob+", created_at = "+created_at+", longitude = "+longitude+", latitude = "+latitude+", mobile = "+mobile+"]";
    }
}
