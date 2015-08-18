# Parallels Desktop Cloud Plugin #

This Jenkins plugin allows to launch Parallels Desktop virtual machines dynamically when they are needed to build a job, and then suspend them back during idle. This way you can achieve high density of resource utilization.

Additionally, if you develop cross-platform software that targets OS X and iOS among others, then you probably know that you can't run OS X virtual machines on regular PC hardware. So you have to buy Mac Minis or Mac Pros anyway to do continuous integration. With this plugin you can just run all your builds on a "homogenous" setup and worry less about the infrastructure.

Guest OS support:
* OS X 10.6 and above
* Windows 2000 SP3 and above
* CentOS 5, Debian 7, Ubuntu 10.04 and above

Requirements:
* [Jenkins LTS](https://jenkins-ci.org/changelog-stable), 1.609.2 or later. Previous versions may work, but are not tested
* [Parallels Desktop 11](http://www.parallels.com/products/desktop/) Pro or Business edition

## Installation ##

This plugin is not yet available in the jenkins plugin "hub", but it will be there really soon. Until that time, you can install the plugin easily by uploading the binary to Jenkins through Plugin Manager UI.

To do this:
* Download the binary release here: [parallels-desktop.hpi](https://github.com/Parallels/jenkins-parallels/releases/download/v0.1.0/parallels-desktop.hpi)
* Go to Manage Jenkins->Manage Plugins
* Switch to "Advanced" tab
* In the "Upload Plugin" section, pick the binary downloaded in the first step and press "Upload"

## Configuration ##

First you need to configure a host machine on which Parallels Desktop is installed. Enable "Remote Login" in OS X "Sharing" settings to allow incoming SSH connections.

Then make sure that you have Java 1.7.0 or greater on your virtual machines. Otherwise jenkins will be unable to start slaves on them.

### Cloud Configuration ###

* Go to Manage Jenkins->Configure System
* Scroll down to the "Cloud" Section and press "Add a new cloud"
* Then configure connection to the host with Parallels Desktop (substitute host name and other parameters to your liking)

![alt tag](https://raw.githubusercontent.com/Parallels/jenkins-parallels/master/src/main/resources/cloud_config.png?token=AGasieKI4XsJMblErXOWrlR2n7QOlyzxks5V3HPqwA%3D%3D)

### Slave Configuration ###

* In the same "Cloud" section, under "Virtual Machines" press "Add"
* In Virtual Machine ID you can either spacify VM name or UUID (which you can find from terminal by typing "prlctl list -a")
* Fill in the rest of config as you would for the regular slave, but skip "Host", since it will be configured dynamically
* Specify Labels that you will bind "cloud" jobs to

![alt tag](https://raw.githubusercontent.com/Parallels/jenkins-parallels/master/src/main/resources/slave_config.png?token=AGasiSnBRAeyZgiq8VkF3CSicTs97cfyks5V3HPFwA%3D%3D)

## Usage ##

Now in any of the build jobs you can set Labels to one of those you configured in your VMs during Slave Configuration step.

When your job is scheduled and there are not enough executors to perform the build, the plugin will find a suitable virtual machine, start it and use it to build the job. Then after approximately 1 minute of inactivity the VM will be stopped.

## FAQ ##

## Bugs, Pull Requests and Contacts ##

Feel free to file bugs or change requests here:
https://github.com/Parallels/jenkins-parallels
