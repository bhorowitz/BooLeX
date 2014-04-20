import play.Project._

name := "website"

version := "1.0"

coffeescriptOptions := Seq("bare")

// unmanagedClasspath in Compile += baseDirectory.value / "../dsl/src"

unmanagedSourceDirectories in Compile  += baseDirectory.value / "../dsl/src"

// unmanagedClasspath in Compile += baseDirectory.value / "../dsl/gen"

unmanagedSourceDirectories in Compile  += baseDirectory.value / "../dsl/gen"

playScalaSettings