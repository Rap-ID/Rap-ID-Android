package com.hackerchai.rapid;

/**
 * Created by hackerchai on 15-2-9.
 */
public class TokenParse {

    public String data;
    public Error error;

    public void setError(Error error) {
        this.error = error;
    }
    public Error getError()
    {
        return error;
    }

    public void setData(String data)
    {
       this.data=data;
    }
    public String getData()
    {
        return data;
    }



    public static class Error {

        private String id;

        private String msg;

        public String getId()
        {
            return id;
        }
        public void setId(String id)
        {
            this.id=id;
        }

        public String getMsg()
        {
            return msg;
        }
        public void setMsg(String msg)
        {
            this.msg=msg;
        }
    }



}
