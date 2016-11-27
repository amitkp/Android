package com.nordusk.webservices;

/**
 * Created by NeeloyG on 11/27/2016.
 */
public class UserTrace {

        private String id;

        private String username;

        private String name;

        private String mobile_no;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getUsername ()
        {
            return username;
        }

        public void setUsername (String username)
        {
            this.username = username;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getMobile_no ()
        {
            return mobile_no;
        }

        public void setMobile_no (String mobile_no)
        {
            this.mobile_no = mobile_no;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", username = "+username+", name = "+name+", mobile_no = "+mobile_no+"]";
        }
}
