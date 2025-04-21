mvn install:install-file -Dfile=lib/processing/core.jar -DgroupId=org.processing -DartifactId=core -Dversion=0.0.0 -Dpackaging=jar &&
mvn install:install-file -Dfile=lib/processing/postfx/PostFX.jar -DgroupId=org.cansik -DartifactId=postfx -Dversion=0.0.0 -Dpackaging=jar &&
mvn install:install-file -Dfile=lib/processing/sound/library/sound.jar -DgroupId=org.processing -DartifactId=sound -Dversion=0.0.0 -Dpackaging=jar &&
mvn install:install-file -Dfile=lib/processing/sound/library/jsyn-20171016.jar -DgroupId=jsyn -DartifactId=jsyn -Dversion=0.0.0 -Dpackaging=jar &&
mvn install:install-file -Dfile=lib/processing/sound/library/javamp3-1.0.3.jar -DgroupId=javamp3 -DartifactId=javamp3 -Dversion=0.0.0 -Dpackaging=jar &&
mvn install:install-file -Dfile=lib/jxinput-1.0.0.jar -DgroupId=com.github.strikerx3 -DartifactId=jxinput -Dversion=1.0.0 -Dpackaging=jar &&
mvn clean install package
