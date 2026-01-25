# Script para alternar para o JDK 17 no PowerShell
$jdk17 = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:JAVA_HOME = $jdk17
$env:PATH = "$jdk17\bin;" + $env:PATH
Write-Host "JAVA_HOME definido para: $jdk17"
java --version