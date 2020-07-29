package com.bundles_powering_IC_NXT.try1

class Net(pins: Vector[Block])

class Block(io: Vector[Net], nets: Vector[Net], insts: Vector[Block])

class Bundling(fp: Vector[Block]) {

  class Bundle(nets: Vector[Net])

  class Term(insts: Vector[Block])

  class Segment; 

  class Topo; // recursive ABT?
}

