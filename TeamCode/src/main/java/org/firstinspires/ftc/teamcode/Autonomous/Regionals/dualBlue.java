package org.firstinspires.ftc.teamcode.Autonomous.Regionals;

import org.firstinspires.ftc.teamcode.SkystoneLinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Blue Dual", group = "auto") // BLUE SIDE

public class dualBlue extends SkystoneLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //sets up imu and vuforia
        init(hardwareMap, true);
        initBitmapVuforia();

        int pos = 0;
        double adjust = 0.0;
        double longAdjust = 0.0;

        waitForStart();

        pos = detectSkystoneOnePix(getBitmap(), true); //DETECT SKYSTONE

        adjustForSkystone(pos, true); //MOVE ROBOT FORWARD OR BACKWARD ALONG WALL TO LINE UP WITH SKYSTONE

        longAdjust = forLongAdjust(pos, true) + 100;

        strafeAdjust(0.6, 2, 0, true);

        turnPID(-90, 0.6 / 90, 0.0001, 2, 2000);

        driveAdjust(270, 0.4, 61, 7); //GO TO STONES

        grabStoneBlue(pos, false); //GRAB SKYSTONE

        if (pos == -1)
            driveAdjust(270, -0.8, 1, 7);
        else
            driveAdjust(270, -0.8, 8, 7); //MOVE BACKWARD

        turnPID(0, 0.6 / 90, 0.0001, 2, 2000);

        driveAdjust(0, 0.5, longAdjust + 50, 4); // MOVE OTHER SIDE

        hook(false, false); // drop stone

        turnPID(90, 0.6 / 90, 0.0001, 2, 1500);

        driveAdjust(90, -0.5, 5, 2); // DRIVE TO FOUNDATION

        foundationD(true);

        driveAdjust(90, 0.8, 40, 4);

        turnPID(180, 0.6 / 90, 0.0001, 2, 2000);

        driveAdjust(180, -0.6, 10, 1); // MAYBE USE DRIVE FORWARD

        foundationD(false);

        driveAdjust(180, 0.5, 3, 1);

        strafeAdjust(0.6, 10, 0, false);

        driveAdjust(180, 0.6, 10, 2);

        /* +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+ second stone

        // MOVE BACK TO STONE SIDE
        switch (pos) {
            case -1:
                driveAdjust(0, -0.6, longAdjust + 37, 3);
                break;
            case 0:
                driveAdjust(0, -0.6, longAdjust + 39, 3);
                break;
            case 1:
                driveAdjust(0, -0.6, longAdjust + 41, 3);
                break;
        }
        //driveAdjust(180, -0.8, longAdjust + 31, 3);

        //sleep(250);
        //turnPID(-90, 0.5/360,0.001,2,4000);
        turnPID(-90, 0.6/90,0.0001,2,1500);

        if (pos == 1) strafeAdjust(0.4,2,270,true); // strafe

        driveAdjust(270, 0.4, 30, 5); //GO TO STONES

        grabStoneBlue(pos, false);

        driveAdjust(270,-0.4,11, 7); //MOVE BACKWARD

        // MOVE TO OTHER SIDE
        switch (pos) {
            case -1:
                //turnPID(175, 0.6/360,0.001,2,3000);
                turnPID(0, 0.6/90,0.0001,2,2000);
                driveAdjust(0, .8, longAdjust + 55, 3000); // MOVE OTHER SIDE
                break;
            case 0:
                turnPID(0, 0.6/90,0.0001,2,2000);
                driveAdjust(0, .8, longAdjust + 50, 3000); // MOVE OTHER SIDE
                break;
            case 1:
                //turnPID(-178, 0.6/360,0.001,2,3000);
                turnPID(0, 0.6/90,0.0001,2,2000);
                driveAdjust(0, .8, longAdjust + 57, 3000); // MOVE OTHER SIDE
                break;
        }

        foundationD(true);

        driveAdjust(0, -0.8, 20, 2); //park

        sleep(100);

        strafeAdjust(.4,8,0,true);

         */

        telemetry.addData("auto:", "complete");
        telemetry.update();
    }
}
