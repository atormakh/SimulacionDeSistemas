N=100
L=100
R=2
RC=10
BF=false
periodic=false
for i in "$@"; do
  case $i in
    --N)
      shift
      N=$1
      shift
      ;;
    --L)
      shift
      L=$1
      shift
      ;;
    --R)
      shift
      R=$1
      shift
      ;;
    --RC)
      shift
      RC=$1
      shift
      ;;
    --periodic)
      shift
      periodic=true
      shift
      ;;
    -bf)
      BF=true
      shift
      ;;
    *)
      ;;
  esac
done


java -cp target/simulacion_sistemas-1.0-SNAPSHOT.jar -DN=$N -DL=$L -DR=$R -DRC=${RC} -Dbf=${BF} -Dperiodic=${periodic} cellIndexMethod.Main

python3 cellIndexMethod.py