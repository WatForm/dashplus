{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils"; # used to generalize for all hardware
  };

  outputs =
    { self, nixpkgs, flake-utils, ... }:
    
    flake-utils.lib.eachDefaultSystem (system:
    let
      pkgs = nixpkgs.legacyPackages.${system};
      jdk = pkgs.jdk25; # minimum version of java on which this is confirmed to work
    in
    {
      devShells.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          jdk
          gradle # build tool for the project
          alloy # enables the alloy5 command, which brings up the UI
          alloy6 # enables the alloy6 command, which brings up the UI
          tlaplus18 # running tla on the command line
        ];

        JAVA_HOME = "${jdk}"; # sets environment variable for gradle

        shellHook = ''
          echo "Welcome to the nix shell for dashplus development!"
          echo "alloy5 -> run alloy 5 GUI"
          echo "alloy6 -> run alloy 6 GUI"
          echo "tlc -> run the TLA model checker on the command line"
          echo "build -> build the source into a jar file"
          echo "run [args] -> run the built jar with [args] passed through"

          alias build="./gradlew releasejar"
          run()
          {
            java -jar ./app/build/libs/watform-dashplus.jar "$@";
          }
        '';
      };
    });
}
