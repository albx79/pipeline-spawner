package com.sky.ukiss.spawner.shared.rpc.server

import com.sky.ukiss.spawner.shared.model.auth.UserToken
import com.sky.ukiss.spawner.shared.rpc.server.open.AuthRPC
import com.sky.ukiss.spawner.shared.rpc.server.secure.SecureRPC
import io.udash.i18n._
import io.udash.rpc._

@RPC
trait MainServerRPC {
  /** Returns an RPC for authentication. */
  def auth(): AuthRPC

  /** Verifies provided UserToken and returns a [[SecureRPC]] if the token is valid. */
  def secure(token: UserToken): SecureRPC

  /** Returns an RPC serving translations from the server resources. */
  def translations(): RemoteTranslationRPC
}

object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]