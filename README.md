<h1 align="center">
    Control-pc
</h1>
<h4 align="center">
  A simple way to control your pc using a cellphone.
</h4>
<p align="center">
  <img alt="Front-end image" src="https://i.imgur.com/7QpEFGm.png">
</p>

## The problem

I wanted a way to control my pc while i'm lying in bed, without the use of a wireless keyboard/mouse. I would use it primarily when i'm watching some movie/series, to play/pause it or control the volume.

## The solution

Considering that i wanted to play/pause the video and control the volume, i thought that the best way to do this is using the Fn keys on the keyboard. After searching the simplest way to do it, using the programming languages that i already know, i ended with the combination of [Java](https://www.java.com/) + [Autohotkey](https://www.autohotkey.com/). I use [Java](https://www.java.com/) for the server and basic keyboard/mouse controls and [Autohotkey](https://www.autohotkey.com/) to trigger the Fn keys, since i couldn't find an easy way to do this with [Java](https://www.java.com/).

The easiest way that i found to access the server, running on my PC, from my cellphone is by [fixing](https://portforward.com/networking/static-ip-windows-10.htm) my IP address. After doing that, i can simply use that IP to access a front-end page that i made with HTML+JS+Jquery which contains some controls that send the commands to the server.

## Technologies

- [Java](https://www.java.com/)
- [SparkJava](http://sparkjava.com/)
- [Autohotkey](https://www.autohotkey.com/)
- [Jquery](https://jquery.com/)

## Requirements

- [Java 1.8+](https://www.java.com/)
- [Java JDK 1.8+](https://www.oracle.com/br/java/technologies/javase/javase-jdk8-downloads.html)
- [Autohotkey](https://www.autohotkey.com/)
- Some IDE with [Maven](https://maven.apache.org/index.html), like [Eclipse](https://www.eclipse.org/)

## How to use

1. Clone the repository:

```bash
$ git clone https://github.com/nogenem/control-pc.git
```

2. Open the project with an IDE / Code editor.

   - With [Eclipse](https://www.eclipse.org/), you should open it in the folder that you did the `git clone` and then go into `File > Import... > Maven > Existing Maven Projects...`.
     - Make sure that the project is running with JRE 1.8+.
       - Right click the "JRE System Library" and click on "Properties".
       - Check if the "JavaSE-1.8 (jdk1.8.x_xxx)" is selected, or a higher version.

3. Copy the file `.env.example` and rename it to `.env`.

   - You can edit the values inside the file as you please, but the default ones will work fine.
   - If you want to create a new `KeyStore.jks` file, you can use the following command:

```bash
$ keytool -genkey -alias control-pc -keyalg RSA -keystore KeyStore.jks -keysize 2048
```

4. Open the Main.java file and click on "Run as...".
5. You can now open [http://localhost:7777/](http://localhost:7777/) and start controlling your PC.
   - If you [fixed](https://portforward.com/networking/static-ip-windows-10.htm) your IP address, you can access the front-end on your cellphone too, using the same port (7777).

**_obs_**: If you just want to run the project, you could download the [latest release](https://github.com/nogenem/control-pc/releases) to try it. You will still need to fulfill the requirements.
