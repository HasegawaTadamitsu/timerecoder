
setenv JAVA_HOME    /usr/lib/jvm/java-1.6.0-openjdk
setenv ANDROID_HOME ~/work/android/android-sdk-linux_86_r04

alias mvn_deploy mvn com.jayway.maven.plugins.android.generation2:maven-android-plugin:deploy
alias start_emulator ${ANDROID_HOME}/tools/emulator -avd Android_1_6
alias start_SI01 ${ANDROID_HOME}/tools/emulator -avd JNDK01 -qemu --cpu cortex-a8
alias start_androidx86  ${ANDROID_HOME}/tools/adb connect patrush-android:5555

alias start_debugger ${ANDROID_HOME}/tools/ddms
alias compile_all 'mvn clean &&mvn package&&mvn_deploy'

