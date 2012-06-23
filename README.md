# jFactory

jFactory is a factory library for Java, inspired by the great [factory_girl](https://github.com/thoughtbot/factory_girl) by Thoughtbot.

jFactory can be used to easily define factories for your model objects.
It has a notion of building and creating objects, where creating objects means saving objects to a database after building them.

jFactory is database/persistence layer agnostic, support for databases can easily provided by subclassing the PersistableObjectFactory class.
Currently only MongoDB is supported through the [Morphia](http://code.google.com/p/morphia/) POJO mapper.
