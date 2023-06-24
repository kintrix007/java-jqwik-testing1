{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  packages = [
    pkgs.gradle
    pkgs.jdk
    pkgs.geckodriver
  ];
}
