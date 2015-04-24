package com.hackerchai.rapid;

/**
 * Created by hackerchai on 15-2-9.
 */
public class TokenParse {
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

        private String token;

        private String uid;

        public String getUid()
        {
            return uid;
        }
        public void setUid(String uid)
        {
            this.uid=uid;
        }

        public String getToken()
        {
            return token;
        }
        public void setToken(String token)
        {
            this.token=token;
        }
    }



}
