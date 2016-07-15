mypkgs:
  pkg.installed:
    - pkgs:
      - java-1.7.0-openjdk
      - java-1.7.0-openjdk-devel

rmpks:
  pkg.removed:
    - pkgs:
      - jre1.8.0_45

java-alternative:
  cmd.run:
    - name: 'update-alternatives --set java /usr/lib/jvm/jre-1.7.0-openjdk.x86_64/bin/java'
