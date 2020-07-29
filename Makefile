# Use sbt, but this is for educational purposes to make explicitly clear what is being compiled into the SCells -- scala (spread) sheets program

all:
	time fsc Model.scala Formulas.scala FormulaParsers.scala Spreadsheet.scala Main.scala Evaluator.scala Arithmetic.scala

run:
	scala -cp . org.stairwaybook.scells.SCells &

