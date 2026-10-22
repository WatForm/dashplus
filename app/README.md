# README.md

To create multiple apps from within this repo:

1. Within your tool's directory, make a cli file, e.g., dashtoalloy/DashToAlloyCli.java

2. In build.gradle (file in this directory), add to the following using your cli for the mainClass and excluding directories that are not part of your project.  Every project will need the core Alloy directories.

def tools = [
    dpalloy: [
        mainClass: "ca.uwaterloo.watform.dpalloycli.DpAlloyCli",
        excludes: [
            'ca/uwaterloo/watform/alloytotla/**',
            'ca/uwaterloo/watform/cli/**',
            'ca/uwaterloo/watform/dashast/**',
            'ca/uwaterloo/watform/dashmodel/**',
            'ca/uwaterloo/watform/dashexprvisitor/**',
            'ca/uwaterloo/watform/dashtoalloy/**',
            'ca/uwaterloo/watform/dashtotla/**',
            'ca/uwaterloo/watform/predabstraction/**',
            'ca/uwaterloo/watform/tlaast/**',
            'ca/uwaterloo/watform/tlamodel/**'
        ]
    ],
    dashtoalloy: [
       mainClass: "ca.uwaterloo.watform.dashtoalloy.DashToAlloyCli",
       excludes: [
            'ca/uwaterloo/watform/alloytotla/**',
            'ca/uwaterloo/watform/cli/**',
            'ca/uwaterloo/watform/dashtotla/**',
            'ca/uwaterloo/watform/dpalloy/**',
            'ca/uwaterloo/watform/predabstraction/**',
            'ca/uwaterloo/watform/tlaast/**',
            'ca/uwaterloo/watform/tlamodel/**'
        ]
    ],
    releaseJar: [
       mainClass: "ca.uwaterloo.watform.cli.Main",
       excludes: []
    ]
]

3. Build your tool using something like: './gradlew dpalloy'

4. Run tool using something like: `java -ea -jar /Users/nday/UW/github/dashplus/dashplus/app/build/libs/dpalloy.jar`
