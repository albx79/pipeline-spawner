package com.sky.ukiss.pipelinespawner

import com.sky.ukiss.pipelinespawner.api.Job
import japgolly.scalajs.react
import japgolly.scalajs.react.vdom.html_<^._

object JobInfo {
  type Props = Job
  type State = Unit

  def render(state: State, props: Props) = {
    <.div(
      <.h1(
        ^.className := "display-1"
      )(s"Job ${props.id}"),
      <.p(
        ^.className := "lead"
      )(
        props.name
      ),
      <.div(
        <.pre(
          """
            |Ideally, job output will be printed here.
            |Lorem ipsum dolor sit abet. Ex falso quod libet.
          """.stripMargin
        )
      )
    )
  }

  def Component = react.ScalaComponent.builder[Props]("JobInfo")
    .initialState(())
    .noBackend
    .render($ => render($.state, $.props))
    .build

}