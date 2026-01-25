# Script para alternar para o JDK 25 no PowerShell
$jdk25 = "C:\Program Files\Eclipse Adoptium\jdk-25.0.1.8-hotspot"
$env:JAVA_HOME = $jdk25
$env:PATH = "$jdk25\bin;" + $env:PATH
Write-Host "JAVA_HOME definido para: $jdk25"
java --version