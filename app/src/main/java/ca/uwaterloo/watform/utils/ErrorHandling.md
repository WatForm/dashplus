# Error Handling


## TL;DR

* determine if the potential error situation is definitely an implementation error or could be caused by user input (parsing, CLI)

* if it is definitely an implementation error
    a) see if there is a generic version of the error in ImplementationError.java
    b) see if there is a package version of it in XImplError.java (extension of ImplementationError), where X is the package 
    c) add it to XImplError.java as a static method 
    d) throw this method as the error situation in your code

* if it **could** be a user error (result of parsing or CLI)
    a) see if there is a package version of it in XError.java (extension of UserOrImplError), where X is the package 
    b) add it to XError.java as a static method 
    c) throw this method as the error situation in your code

* if the error is definitely a user error (e.g., file not found)
    a) see if there is a generic version of it in UserError.java
    b) see if there is a package version of it in XUserError.java (extension of UserOrImplError), where X is the package 
    b) add it to XUserError.java as a static method 
    c) throw this method as the error situation in your code

* exceptions that are UserOrImplError error will be caught and added to the Reporter for elegant output

* any other exceptions are caught in main and printed with stack trace (-debug) or without

* we make little effort to catch Java exceptions as these are usually implementation errors.  We do catch IOExceptions for files not found.


## Intro

* This file describes error handling via exceptions used in this codebase.

* Class Structure for exceptions
    - Err (from AlloyAnalyzer)
    - DashPlusException
        - ImplementationError (file per package)
            - AlloyModelImplError 
            - etc.
        - UserOrImplError (file per package)
            - AlloyCtorError (within alloyast)
            - AlloyModelError (within alloyast)
            - AssumptionError (within alloyast)
            - etc.
        - UserError 
            - CliError

* Reporter is a class for collecting user error messages and warnings.

* Use Pos whenever possible to make error messages more informative

* within package exception constructors are private


## Err

* these are exceptions thrown by the **Alloy Analyzer** code base
* Main.java catches these exceptions and prints them (with stacktrace if -debug)
* they don't appear anywhere in this codebase except Main.java and AlloyInterface.java


## [DashPlusException](/app/src/main/java/ca/uwaterloo/watform/utils/DashPlusException.java)

* every exception thrown in this codebase is in a **subclass** of DashPlusException
* DashPlusException extends RuntimeException
* DashPlusException is **never thrown directly** only through subclasses


## [ImplementationError](/app/src/main/java/ca/uwaterloo/watform/utils/ImplementationError.java)

* ImplementationErrors reflect an **error in code** (definitely NOT caused user input)
* a subclass of DashPlusException
* some general ones in utils/ImplementationError.java
* some **organized into a file within a package** as a subclasses of ImplementationError, e.g., AlloyModelImplError.java
* These should not be caught and should propagate directly to Main to exit with the corresponding exit code.
    - [cli.Main](/app/src/main/java/ca/uwaterloo/watform/cli/Main.java)


### [UserOrImplError] (/app/src/main/java/ca/uwaterloo/watform/utils/UserOrImplError.java)
)
* errors that **could be user errors or implementation errors**
* a subclass of DashPlusException
* never thrown directly, only through subclasses 


### [UserError](/app/src/main/java/ca/uwaterloo/watform/utils/UserError.java)

* errors that are definitely user errors
* currently only used in CliError to catch parameter errors, which are caught in Main.java

### [Reporter](/app/src/main/java/ca/uwaterloo/watform/utils/Reporter.java)

- Wherever a UserOrImplError is a user error it is caught and added to Reporter
```
catch (AlloyCtorError alloyCtorError) {
    Reporter.INSTANCE.addError(alloyCtorError);
}
```
- `Reporter.exitIfHasError()` will throw a Java AbortSignal exception if the Reporter has collected Errors

- '-debug' will not print a stacktrace for these errors

