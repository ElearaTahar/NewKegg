# NewKegg
**NewKegg** is a desktop KEGG metabolic pathways browser.

## Introduction

Using [KGML](https://www.kegg.jp/kegg/xml/) files from the [KEGG](https://www.genome.jp/kegg/) database 
(available in the [kegg.zip](https://github.com/SeverineLiegeois/NewKegg/blob/master/kegg.zip) archive), **NewKegg** displays maps of metabolic pahtways present in bacterias.

## Usage

* Download and unzip the [kegg.zip](https://github.com/SeverineLiegeois/NewKegg/blob/master/kegg.zip) archive.
* Run **NewKegg** from the provided [JAR](https://github.com/SeverineLiegeois/NewKegg/blob/master/NewKegg.jar):

	   `java -jar NewKegg.jar`
	
* Select the unzipped *kegg* directory and wait until processing is done.
* Once all the files have been parsed, a list of pathways is displayed on the left. Clicking on one pathway shows the graph on the right, and the list of species having this pahtway is given.
* One or more species can be selected. The reactions happening in these species are colored.

![Screenshot](/screenshot.png)
	
## Notes

* I wrote this project for an assignment in 2017, during my first year of Bioinformatics Master.
* This project uses the [GraphStream](http://graphstream-project.org/) library.
* It has been developped under Java SE 8 1.8.0.
