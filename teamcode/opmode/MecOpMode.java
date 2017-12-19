package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.teamcode.control.Mecanum;

/**
 * Created by tpasche on 10/17/2017.
 */

@TeleOp(name = "Mecanum", group = "teleop")
public class MecOpMode extends LinearOpMode
{

        private DcMotor DRIVE_FRONT_RIGHT;
        private DcMotor DRIVE_FRONT_LEFT;
        private DcMotor DRIVE_BACK_RIGHT;
        private DcMotor DRIVE_BACK_LEFT;

//        private final double BACK_WHEEL_REDUCE = 0.25;
        private final double BACK_WHEEL_REDUCE = 0.3;

        private Servo armServo;
        private Servo jewelArm;

//        private NormalizedColorSensor colorsensor;
//        private ColorSensor color_sensor;

        private static final double ARM_RETRACTED_POSITION = 0.002;
        private static final double ARM_EXTENDED_POSITION = 0.65;
        private static final double JEWEL_ARM_RETRACTED_POSITION = 0.6;
        private static final double JEWEL_ARM_EXTENDED_POSITION = 0.1;

        @Override
        public void runOpMode () throws InterruptedException
        {
            DRIVE_FRONT_RIGHT = hardwareMap.dcMotor.get("DRIVE_FRONT_RIGHT");
            DRIVE_FRONT_LEFT = hardwareMap.dcMotor.get("DRIVE_FRONT_LEFT");
            DRIVE_BACK_RIGHT = hardwareMap.dcMotor.get("DRIVE_BACK_RIGHT");
            DRIVE_BACK_LEFT = hardwareMap.dcMotor.get("DRIVE_BACK_LEFT");
//  sets left side to opposite of right side and gamepad y stick 1 to -1 top to bottom
            DRIVE_FRONT_LEFT.setDirection(DcMotor.Direction.REVERSE);
            DRIVE_BACK_LEFT.setDirection(DcMotor.Direction.REVERSE);
//>>>>20171122--need left stick y up to be forward
//            DRIVE_FRONT_RIGHT.setDirection(DcMotor.Direction.REVERSE);
//            DRIVE_BACK_RIGHT.setDirection(DcMotor.Direction.REVERSE);


            armServo = hardwareMap.servo.get("armServo");
            jewelArm = hardwareMap.servo.get("jewelArm");

            armServo.setPosition(ARM_RETRACTED_POSITION);
            jewelArm.setPosition(JEWEL_ARM_RETRACTED_POSITION);

//            colorsensor = hardwareMap.get(NormalizedColorSensor.class, "colorsensor");
//            color_sensor = hardwareMap.get(ColorSensor.class, "colorsensor");
/*
            // switch on light on th color sensor
            if(color_sensor instanceof SwitchableLight)
            {
                ((SwitchableLight)color_sensor).enableLight((true));
                telemetry.addLine("Turn sensor light on!");
            }
*/
            telemetry.addLine("MecOpMode: 20171122 ");
            waitForStart();

            while(opModeIsActive())
            {
/*
                DRIVE_FRONT_RIGHT.setPower(-gamepad1.right_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x);
                DRIVE_FRONT_LEFT.setPower(gamepad1.right_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x);
                DRIVE_BACK_RIGHT.setPower(-gamepad1.right_stick_y + -gamepad1.right_stick_x + gamepad1.left_stick_x);
                DRIVE_BACK_LEFT.setPower(gamepad1.right_stick_y + -gamepad1.right_stick_x + gamepad1.left_stick_x);
*/
//>>>>20171122
                double lsx =  gamepad1.left_stick_x;
                double lsy = -gamepad1.left_stick_y;
                double rsx =  gamepad1.right_stick_x;
                double rsy = -gamepad1.right_stick_y;

                setDriveForMecanum(
                        Mecanum.joystickToMotion(
/*
                                gamepad1.left_stick_x,
                                -gamepad1.left_stick_y,
                                gamepad1.right_stick_x,
                                -gamepad1.right_stick_y
*/
                            lsx, lsy, rsx, rsy
                        )
                );

                telemetry.addLine("Joy Sticks: ")
                        .addData("LS_X:", "%.3f", lsx)
                        .addData("LS_Y:", "%.3f", lsy)
                        .addData("RS_X:", "%.3f", rsx)
                        .addData("RS_Y:", "%.3f", rsy);

                telemetry.addLine("Wheel Powers: ")
                        .addData("FL:", "%.3f", DRIVE_FRONT_LEFT.getPower())
                        .addData("FR:", "%.3f", DRIVE_FRONT_RIGHT.getPower())
                        .addData("BL:", "%.3f", DRIVE_BACK_LEFT.getPower())
                        .addData("BR:", "%.3f", DRIVE_BACK_RIGHT.getPower());
                telemetry.update();


//                telemetry.addLine("Check Servo motor");
//                telemetry.update();

                if(gamepad2.a)
                {
                    armServo.setPosition(ARM_RETRACTED_POSITION);
                    telemetry.addLine("A button pressed");
                }
                if(gamepad2.b)
                {
                    armServo.setPosition(ARM_EXTENDED_POSITION);
                    telemetry.addLine("B button pressed");
                }
                telemetry.update();

//                jewelArm.setPosition(JEWEL_ARM_EXTENDED_POSITION);

                idle();
            }
        }

        public void setDriveForMecanum(Mecanum.Motion motion)
        {
    // Added back wheel reduction
            Mecanum.Wheels wheels = Mecanum.motionToWheels(motion);
            DRIVE_FRONT_LEFT.setPower(wheels.frontLeft);
            DRIVE_FRONT_RIGHT.setPower(wheels.frontRight);
            DRIVE_BACK_LEFT.setPower((1.0 - BACK_WHEEL_REDUCE)*wheels.backLeft);
            DRIVE_BACK_RIGHT.setPower((1.0 - BACK_WHEEL_REDUCE)*wheels.backRight);
        }
}