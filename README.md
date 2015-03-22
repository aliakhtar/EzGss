# EzGss

A tool for generating java CssResource files from .gss stylesheets, for 
[GWT](http://www.gwtproject.org).

Takes the following:

```css
.foo{}
.foo-bar-1{}
.foo_bar_2{}
.switch{} /*Java reserved keyword*/
```
    
and produces:    

    package test;

    import com.google.gwt.resources.client.CssResource;

    public interface Test extends CssResource
    {
        @ClassName("foo")
        String foo();

        @ClassName("foo-bar-1")
        String fooBar1();

        @ClassName("foo_bar_2")
        String fooBar2();

        @ClassName("switch")
        String switch_();
    }

##Install

Just clone the repo, then run `mvn clean install` from command line.

To install as well as copy the resulting jar to the home directory (on Linux), run
`compile.sh`.

##Usage

`java -jar ezGss.jar <source> <dest> [package]`

`source` is the path to the `.gss` file, `dest` is the path where the resulting 
.java file will be generated, and `package` is an optional argument to supply the 
package of the `.java` file.

`dest` can be the directory where the file should be created, the file/class name 
can be inferred from the .gss file. E.g `foo-bar.gss` will be translated to `FooBar.java`.
However, the full name can also be specified if necessary.
 
The paths can either be absolute, or they can be relative to the working directory.
For example, if your .gss file is located at `~/project/src/main/resources/com/foo/my-styles.gss`,
and you'd like `~/project/src/main/java/com/foo/MyStyles.java` to be created, you can
run the following:

`cd ~/project/src/main #Switch to the project directory if necessary`

`java -jar /path/to/ezGss.jar resources/com/foo/my-styles.gss java/com/foo com.foo`

This will generate the file `MyStyles.java` in `~/project/src/main/resources/com/foo`.
The name `MyStyles` is inferred from `my-styles.gss`, though an absolute name
can also be provided, e.g: 

`java -jar ezGss.jar resources/com/foo/my-styles.gss java/com/foo/AnotherName.java com.foo`

If the file or directories specified in `dest` don't exist, they will be created. If
the file already exists, it will be overwritten. For example, if the directories `java/com/foo`
didn't exist when the above command was run, they would be created.


##Conversion of CSS class names to Java methods

All css class names would be converted from hyphens or underscores to camelCase. E.g:
`foo-bar` will become `fooBar`, `foo_bar` will become `fooBar`, `FOOBAR`, `fooBar`, 
 and `FOO_BAR` will be unchanged, however **`camelCase_1` will become `camelcase1`** .
 (Notice that all letters were lower cased). 
 
 If a css class is a reserved java keyword, such as `switch`, an underscore
 will be appended to it in the method name, e.g `switch_`.
 
##Recommended 

Create a bash script / batch file containing the commands to run this
tool on all .gss files in your project. Simply run the script each time the Java files
need to be synced with the .gss files.
 
##Known Issues
 
If you have a mix of hyphens/underscores and camelCase in your css class names,
be aware that the resulting java method will be in completely lower case with
hyphens/underscores removed. E.g `.camelCaseTest_1` will have the method 
`camelcasetest1`.

If the file specified in `dest` already exists, it will be overwritten. Its
not recommended to use this tool if you don't have version control set up in your
project.

This library is experimental, please examine the output carefully. Use at
your own risk!

##Wish List

- Ability to run files in batch, e.g to take a directory and run across all the .gss
files inside as well as sub-directories, without needing each file to be specified
individually.

- Generate the `ClientBundle` for the CssResources as well.

Pull requests welcome!

##License

Apache 2.0. Use freely. If you are from Jet Brains,
please consider using this to improve support for GssResources in Intellij.
