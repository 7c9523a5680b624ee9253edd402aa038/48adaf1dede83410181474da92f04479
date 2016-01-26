cd C:\\extreme-ant-cheat-service
set SERVICE_NAME=TaskExtreme
 set PR_INSTALL=C:\\extreme-ant-cheat-service\prunsrv.exe
REM Service log configuration
 set PR_LOGPREFIX=%SERVICE_NAME%
 set PR_LOGPATH=C:\\extreme-ant-cheat-service\logs
 set PR_STDOUTPUT=C:\\extreme-ant-cheat-service\logs\stdout.txt
 set PR_STDERROR=C:\\extreme-ant-cheat-service\logs\stderr.txt
 set PR_LOGLEVEL=Error
REM Path to java installation
 set PR_JVM=
 set PR_CLASSPATH=c:\\extreme-ant-cheat-service\task.jar;c:\\extreme-ant-cheat-service\postgresql.jdbc4.jar;c:\\extreme-ant-cheat-service\jl1.0.1.jar
REM Startup configuration
 set PR_STARTUP=auto
 set PR_STARTMODE=jvm
 set PR_STARTCLASS=br.com.extremeantcheatclan.view.TaskStart
 set PR_STARTMETHOD=main
REM Shutdown configuration
 set PR_STOPMODE=jvm
 set PR_STOPCLASS=br.com.extremeantcheatclan.view.TaskStop
 set PR_STOPMETHOD=main
REM JVM configuration
 set PR_JVMMS=256
 set PR_JVMMX=1024
 set PR_JVMSS=4000
 set PR_JVMOPTIONS=-Duser.language=DE;-Duser.region=de
REM Install service
 prunsrv.exe //IS//%SERVICE_NAME% --DisplayName="Extreme Ant Cheat - Service" --Description="Servico de monitoramento extreme ant cheat"
prunsrv.exe //RS//%SERVICE_NAME%
REM Inicia service
net start TaskExtreme