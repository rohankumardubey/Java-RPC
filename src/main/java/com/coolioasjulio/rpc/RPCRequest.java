package com.coolioasjulio.rpc;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RPCRequest
{
    private long id = 0;
    private boolean instantiate = false;
    private String className = "";
    private String objectName = "";
    private String methodName = "";
    private List<String> argClassNames = new ArrayList<>();
    private List<Object> args = new ArrayList<>();

    public long getId()
    {
        return id;
    }

    public boolean isInstantiate()
    {
        return instantiate;
    }

    public String getClassName()
    {
        return className;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public List<Class<?>> getClasses() throws ClassNotFoundException
    {
        return getClasses(new HashMap<>());
    }

    public List<Class<?>> getClasses(Map<Class<?>,Class<?>> unboxMap) throws ClassNotFoundException
    {
        try
        {
            List<Class<?>> classes = new ArrayList<>();
            for(String className : argClassNames)
            {
                Class<?> clazz = Class.forName(className);
                classes.add(unboxMap.getOrDefault(clazz, clazz));
            }
            return classes;
        }
        catch(ClassNotFoundException e)
        {
            throw e;
        }
    }

    public Object[] getTypedArgs() throws ClassNotFoundException
    {
        List<Class<?>> classes = getClasses();
        List<Object> typedArgs = new ArrayList<>();
        Gson gson = new Gson();

        for(int i = 0; i < classes.size(); i++)
        {
            Object o = args.get(i);
            Class<?> clazz = classes.get(i);
            Object typedArg = gson.fromJson(gson.toJson(o), clazz);
            typedArgs.add(typedArg);
            assert clazz.isInstance(typedArg);
        }

        return typedArgs.toArray();
    }
}