graph-rat
=========

<img src="Graph-RATLogo2.png" height="128" weight="128" alt="Graph-RAT Logo"/> 
# Graph-RAT Programming Environment</h1>

## General Description

Graph-RAT is a development environment for bridging the gap between data mining, recommendation, and machine learning.
At its core, it is a database abstraction layer, abstracting away differences between XML files, database files, and multiple
database sources.  On top of this is a query language that encompasses all SQL queries that do not require temporary storage 
(with guarantees of constant space iterators).  On top of the query system is a language of analysis algorithms that
function as analysis primitives.  These algorithms are controlled by a scheduler (by default, sequential execution of algorithms.)</p>

The benefit of this complexity is the ability to mingle nearly all different approaches to data mining in a single system.  In addition,
certain kinds of analysis are only possible in this system.  For instance, defining subgroups, performing analysis of the real-time-defined
subgroups, then aggregating the results into a global result.

The syntax of the language is a simple XML format.  However, Graph-RAT is designed to utilize GUI tools to write programs - the
tree structure of the analysis is poorly suited to linear text descriptions.  Graph-RAT 0.4.3 provides such a GUI, while 
Graph-RAT 0.5.1 is not yet implemented.

## Current Versions
The current stable branch is 0.4.3, containing most of the analysis algorithms needed for building software.  The current development branch
is 0.5.1 - a complete rewrite of the system adding a query system, a more complete metadata standard for describing algorithms, and a more 
robust and user-friendly system for describing parameters and properties.  0.5.1 is in alpha, currently experiencing stability issues with only 
embedded, not GUI or command-line functioning yet.  This project has been a target for hackers damaging some source.  However, a code review has been performed
and there are no hacker additions to the code itself though older binaries should be scanned before use.

## How to Cite

+ Using an individual algorithm: see the algorithm Javadoc for the citation.
+ Using as a language: [McEnnis, Daniel. 2009. "Graph-RAT programming environment" Waikato University White Paper. 04/2009.](Graph-RAT09.pdf)
+ Using Graph-Triples: [McEnnis, Daniel. 2009. "Linear-time graph triples census algorithm under assumptions typical of social networks." University of Waikato White Paper. 06/2009.](GraphTriplesWorkingPaper.pdf)
