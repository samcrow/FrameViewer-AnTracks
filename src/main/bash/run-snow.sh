#!/bin/bash
#
#  Copyright 2012-2015 Sam Crow
#
# This file is part of Trajectory.
# 
# Trajectory is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# Trajectory is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with Trajectory.  If not, see <http://www.gnu.org/licenses/>.
# 


#
# A shell script that allows Frame Viewer to be easily run on a Java 7 installation
# that is not set as the default java installation.
# This is useful for running on Mac OS X 10.6 Snow Leopard, where Java 7 works
# but is not really supported.
#
# The placeholder values in this file are replaced automatically when the project
# is compiled.
#


# Java information: The end-user may need to customize these with information
# on his/her java installation.
#
# The version of Java that is installed on this computer
JAVA_VERSION='1.7.0_25'
# The Java installation type, either 'jdk' or 'jre'
JAVA_INSTALL_TYPE='jre'


# Current final name of Frame Extractor to run
FRAME_VIEWER_NAME='$name$'

# cd to the location of this file
cd `dirname $0`
# Run the JAR file
/Library/Java/JavaVirtualMachines/$JAVA_INSTALL_TYPE$JAVA_VERSION.$JAVA_INSTALL_TYPE/Contents/Home/bin/java -jar $FRAME_VIEWER_NAME-jfx.jar
