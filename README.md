# ActorFamilyTree

## Description
The ActorFamilyTree project contains of different aspects of functional programming in Scala. The basic idea was to provide a Discord Chat Bot which can communicate within a private Discod server in the main channel. 
The Bot should be able to communicate with each user individually and answer to specific commands.

## Definition of Actors
The base functionality is defined as closed Actor System which uses each actor as specific node with unique functionality
The scope of function can be extracted from the Tree diagram shown below.

![Actor Family Tree](./Resources/Actor_Diagram.png)

## Definition of Streams
To evaluate the Actor System and abd provide some sort of statistics Scala Streams are used. Therefore the Chat Bot was improved to be able to extract written messages from all time and provide them in the Resource/Source.txt. With this Dataset a specific evaluation according to the private Discord Server was enabled.

![Stream Graphic](./Resources/Stream_Graphic.png)

## Definition of Kafka


## Definition of Spark

## Build information
[![Build Status](https://travis-ci.org/FizziR/ActorFamilyTree.svg?branch=master)](https://travis-ci.org/FizziR/ActorFamilyTree) [![Coverage Status](https://coveralls.io/repos/github/FizziR/ActorFamilyTree/badge.svg)](https://coveralls.io/github/FizziR/ActorFamilyTree)



