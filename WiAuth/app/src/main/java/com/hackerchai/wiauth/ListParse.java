package com.hackerchai.wiauth;

import java.util.List;

/**
 * Created by hackerchai on 15-2-9.
 */
public class ListParse{
    private String err_code;

    private String err_msg;

    public Data data;

    public String getErr_code()
    {
        return err_code;
    }
    public void setErr_code(String err_code)
    {
        this.err_code=err_code;

    }

    public String getErr_msg()
    {
        return err_msg;
    }
    public void setErr_msg(String err_msg)
    {
        this.err_msg=err_msg;
    }
    public void setData(Data data)
    {
        this.data=data;
    }
    public Data getData()
    {
        return data;
    }

    public static class Data {



        public String max_id;
        public String min_id;
        public List<Items> items;
        public void setItems(List<Items> items)
        {
            this.items=items;
        }
        public List<Items> getItems()
        {
            return items;
        }
        public void setMax_id(String max_id)
        {
            this.max_id=max_id;
        }
        public String getMax_id()
        {
            return max_id;
        }

        public void setMin_id(String min_id)
        {
            this.min_id=min_id;
        }
        public String getMin_id()
        {
            return min_id;
        }



       public static class Items {
           public String id;
           public String name;
           public String account;
           public String idnum;
           public String mobile;
           public String sex;
           public String usmid;

           public void setId(String id)
           {
               this.id = id;
           }

           public String getId()
           {
               return id;
           }

           public void setName(String name)
           {
               this.name=name;
           }

           public String getName()
           {
               return name;
           }

           public void setMobile(String mobile)
           {
               this.mobile=mobile;
           }

           public String getMobile()
           {
               return mobile;
           }
           public void setUsmid(String usmid)
           {
               this.usmid=usmid;
           }

           public String getUsmid()
           {
               return usmid;
           }
           public void setSex(String sex)
           {
               this.sex=sex;
           }

           public String getSex()
           {
               return sex;
           }
           public void setIdnum(String idnum)
           {
               this.idnum=idnum;
           }

           public String getIdnum()
           {
               return idnum;
           }
           public void setAccount(String account)
           {
               this.account=account;
           }

           public String getAccount()
           {
               return account;
           }


       }
    }



}