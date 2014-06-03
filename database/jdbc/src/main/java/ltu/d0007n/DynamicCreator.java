package ltu.d0007n;

public class DynamicCreator<C> {

	private final String description;
	
    public DynamicCreator(String description) {
		this.description = description;
	}

     /**
     * Dynamically loads the named class (fully qualified class name).
     */
    public Class createClass(String className) throws ClassNotFoundException {
        Class clazz;
        try {
            clazz = Class.forName(className);
            return clazz;

        } catch (ExceptionInInitializerError eiie) {
            String info = "Could not load the " + description + " object: " + className
                    + ". Could not initialize static object in server: ";
            info += eiie.getMessage();
            throw new ClassNotFoundException(info, eiie);

        } catch (LinkageError le) {
            String info = "Could not load the " + description + " object: " + className
                    + ". This object is depending on a class that has been changed after compilation ";
            info += "or a class that was not found: ";
            info += le.getMessage();
            throw new ClassNotFoundException(info, le);

        } catch (ClassNotFoundException cnfe) {
            String info = "Could not find the " + description + " object: " + className + ": ";
            info += cnfe.getMessage();
            throw new ClassNotFoundException(info, cnfe);
        }
    }

    /**
     * Creates an instance from a Class.
     * <p/>
     * Used (here) to create a DataSource object instance from a DataSource class.
     * <p/>
     */
    public C createObject(String className, Class clazz) throws ClassNotFoundException {
        C object;
        try {
            object = (C) clazz.newInstance();

        } catch (InstantiationException ie) {
            String info = "Could not create " + description + " object: " + className
                    + ". Could not access object constructor: ";
            info += ie.getMessage();
            throw new ClassNotFoundException(info, ie);

        } catch (IllegalAccessException iae) {
            String info = "Could not create " + description + " object: " + className
                    + ". Could not instantiate object. Does the object classname refer to an abstract class, "
                    + "an interface or the like?: ";
            info += iae.getMessage();
            throw new ClassNotFoundException(info, iae);

        } catch (ClassCastException cce) {
            String info = "Could not create " + description + " object: " + className
                    + ". The specified object classname does not refer to the proper type: ";
            info += cce.getMessage();
            throw new ClassNotFoundException(info, cce);
        }

        return object;
    }
}
