package config

import enumeratum.{CirisEnum, Enum, EnumEntry}

sealed trait AppEnvironment extends EnumEntry


object AppEnvironment extends Enum[AppEnvironment] with CirisEnum[AppEnvironment] {
  case object Local extends AppEnvironment
  case object Testing extends AppEnvironment
  case object Production extends AppEnvironment

  val values = findValues
}