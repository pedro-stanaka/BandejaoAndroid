Cardápio do RU de Londrina

mvn install:install-file -Dfile=$ANDROID_HOME/platforms/android-19/android.jar -DgroupId=com.google.android -DartifactId=android -Dversion=4.4  -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile="./com/android/support/support-v4/19.0.0/support-v4-19.0.0.jar" -DpomFile="./com/android/support/support-v4/19.0.0/support-v4-19.0.0.pom" -Dpackaging="jar"

mvn install:install-file -Dfile="./com/android/support/appcompat-v7/19.0.0/appcompat-v7-19.0.0.jar" -DpomFile="./com/android/support/appcompat-v7/19.0.0/appcompat-v7-19.0.0.pom" -Dpackaging="jar"
mvn install:install-file -Dfile="./com/android/support/appcompat-v7/19.0.0/appcompat-v7-19.0.0.aar" -DpomFile="./com/android/support/appcompat-v7/19.0.0/appcompat-v7-19.0.0.pom" -Dpackaging="apklib"


mvn install:install-file -Dfile="./com/google/android/gms/play-services/4.4.52/play-services-4.4.52.jar" -DpomFile="./com/google/android/gms/play-services/4.4.52/play-services-4.4.52.pom" -Dpackaging="jar"
mvn install:install-file 
-Dfile="./com/google/android/gms/play-services/4.4.52/play-services-4.4.52.aar" -DpomFile="./com/google/android/gms/play-services/4.4.52/play-services-4.4.52.pom" -Dpackaging="apklib"


