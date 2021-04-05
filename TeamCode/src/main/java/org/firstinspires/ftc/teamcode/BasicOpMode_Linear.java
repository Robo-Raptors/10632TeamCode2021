/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;




/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "2B", group = "Linear Opmode")
public class BasicOpMode_Linear extends LinearOpMode {

    // Declare OpMode members.
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontMotor = null;
    public DcMotor rightFrontMotor = null;
    public DcMotor leftBackMotor = null;
    public DcMotor rightBackMotor = null;
    public DcMotor intakeMotor = null;
    public DcMotor rampMotor = null;
    public DcMotor shootingMotorRight = null;
    public DcMotor shootingMotorLeft = null;
    public CRServo ArmServo = null;
    public Servo ClawServo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFrontMotor = hardwareMap.get(DcMotor.class, "left_front_motor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "right_front_motor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "left_back_motor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "right_back_motor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor");
        rampMotor = hardwareMap.get(DcMotor.class, "ramp_motor");
        shootingMotorRight = hardwareMap.get(DcMotor.class, "shooting_right");
        shootingMotorLeft = hardwareMap.get(DcMotor.class, "shooting_left");
        ArmServo = hardwareMap.crservo.get("Arm_Servo");
        ClawServo = hardwareMap.get(Servo.class, "Claw_Servo");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        rampMotor.setDirection(DcMotor.Direction.REVERSE);
        shootingMotorRight.setDirection(DcMotor.Direction.FORWARD);
        shootingMotorLeft.setDirection(DcMotor.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        ClawServo.setPosition(1);

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            double ly = gamepad1.left_stick_y;
            double lx = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            leftFrontMotor.setPower(ly + lx - rx);
            leftBackMotor.setPower(ly - lx - rx);
            rightFrontMotor.setPower(ly - lx + rx);
            rightBackMotor.setPower(ly + lx + rx);


            double intakePower;
            double rampPower;


            intakePower = gamepad2.right_stick_y;
           // rampPower = gamepad2.left_stick_y;

            // Send calculated power to wheels
            intakeMotor.setPower(intakePower);
            //rampMotor.setPower(rampPower);

            //Servos Start Here
            /*IMPORTANT SERVO UPDATE PLEASE READ.
            Here is the discovery: so unlike last year, whenever we setPosition a servo, it does not actually set a position.
            Instead, the values we put behave a lot more like motors as it moves a direction continuously.
            When we set it "position" to a value of 1, the servo moves one direction forever; set it to 0, the servo moves the other; set it to .5, it stops.
            Idk how this happened, but I believe the cause to be the time we were configuring and setting up the Rev Servos.
            Also, I tried making the controls and programming easier and smooth by adding ELSE statements, however it was being slow because I believe the ping between the phones and controller is too high
            */

            // Using the bumper buttons so that there is no exponential input. Linear input was preferred to
            // keep the shooting mechanism at one variable input.

            if (gamepad2.left_bumper) {
               shootingMotorLeft.setPower(.8);
               shootingMotorRight.setPower(.8);
               rampMotor.setPower(-1);
            } else {
                shootingMotorRight.setPower(0);
                shootingMotorLeft.setPower(0);
                rampMotor.setPower(0);
            }

            //Controlling the ARM and CLAW
            if (gamepad2.a) {
               ArmServo.setPower(1);
            }
            if (gamepad2.y) {
                ArmServo.setPower(-1);
            }
            //Using a continuous servo, so using a button to stop all motion of the servo
            if (gamepad2.dpad_down) {
                ArmServo.setPower(0);
            }

            if (gamepad2.x) {
                ClawServo.setPosition(0);
            }

            if (gamepad2.b) {
                ClawServo.setPosition(1);
            }

            if (gamepad2.right_bumper) {
                rampMotor.setPower(1);
            } else {
                rampMotor.setPower(0);
            }




        }


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor,
                intakeMotor, rampMotor);
        telemetry.update();
    }


}




