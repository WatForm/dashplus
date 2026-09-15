{ pkgs, lib, config, inputs, ... }:

{
  # https://devenv.sh/basics/
  env.GREET = "devenv";

  # https://devenv.sh/packages/
  packages = [ 
    
  ];

  # https://devenv.sh/languages/
  # Enable Java tools
  languages.java.enable = true;
  languages.java.gradle.enable = true;
  languages.java.jdk.package = pkgs.openjdk25; 

  # https://devenv.sh/processes/
  # processes.dev.exec = "${lib.getExe pkgs.watchexec} -n -- ls -la";

  # https://devenv.sh/services/
  # services.postgres.enable = true;

  # https://devenv.sh/scripts/
  scripts.hello.exec = ''
    echo hello from $GREET
  '';

  scripts.a2t.exec = ''
    ./gradlew alloytotla
    java -jar ./app/build/libs/alloytotla.jar ./app/src/test/resources/alloytotla/"$1".als -v -d && cat ./AlloyToTla.log
  '';

  scripts.a2tq.exec = ''
    java -jar ./app/build/libs/alloytotla.jar ./app/src/test/resources/alloytotla/"$1".als -v -d && cat ./AlloyToTla.log
  '';

  # https://devenv.sh/basics/
  enterShell = ''
    hello         # Run scripts directly
    java --version # Use packages
  '';

  # https://devenv.sh/tasks/
  # tasks = {
  #   "myproj:setup".exec = "mytool build";
  #   "devenv:enterShell".after = [ "myproj:setup" ];
  # };

  # https://devenv.sh/tests/
  enterTest = ''
    echo "Running tests"
    git --version | grep --color=auto "${pkgs.git.version}"
  '';

  # https://devenv.sh/git-hooks/
  # git-hooks.hooks.shellcheck.enable = true;

  # See full reference at https://devenv.sh/reference/options/
}
