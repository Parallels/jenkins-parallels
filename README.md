# Parallels Desktop Cloud Plugin #

This Jenkins plugin allows to launch Parallels Desktop virtual machines dynamically when they are needed to build a job, and then suspend them back during idle. This way you can achieve high density of resource utilization.

Additionally, if you develop cross-platform software that targets OS X and iOS among others, then you probably know that you can't run OS X virtual machines on regular PC hardware. So you have to buy Mac Minis or Mac Pros anyway to do continuous integration. With this plugin you can just run all your builds on a "homogenous" setup and worry less about the infrastructure.

Guest OS support:
* OS X 10.6 and above
* Windows 2000 SP3 and above
* CentOS 5, Debian 7, Ubuntu 10.04 and above

Requirements:
* [Jenkins LTS](https://jenkins-ci.org/changelog-stable), 1.609.2 or later. Previous versions may work, but are not tested
* [Parallels Desktop 11](http://www.parallels.com/products/desktop/) Professional or Business edition

## Installation ##

This plugin is not yet available in the jenkins plugin "hub", but it will be there really soon. Until that time, you can install the plugin easily by uploading the binary to Jenkins through Plugin Manager UI.

To do this:
* Download the binary release here: [parallels-desktop.hpi](https://github.com/Parallels/jenkins-parallels/releases/download/v0.0/parallels-desktop.hpi)
* Go to Manage Jenkins->Manage Plugins
* Switch to "Advanced" tab
* In the "Upload Plugin" section, pick the binary downloaded in the first step and press "Upload"

## Configuration ##

## Usage ##

## Bugs, Pull Requests and Contacts ##

Feel free to file bugs or change requests here:
https://github.com/Parallels/jenkins-parallels
