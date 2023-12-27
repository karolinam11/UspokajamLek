import {Component, OnInit} from '@angular/core';
import {AuthService} from "../shared/auth.service";
import {NavigationEnd, Router} from "@angular/router";
import {NotificationService} from "../shared/notification.service";
import {ConfirmationDialogComponent} from "../confirmation-dialog/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {NotificationsComponent} from "../notifications/notifications.component";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  loggedIn = false;
  route: string = '';
  role = null;
  numOfNotifications: number | null = null

  constructor(private authService: AuthService,
              private router: Router,
              private dialog: MatDialog,
              private notificationService: NotificationService
  ) {
    this.authService.user.subscribe(response => {
      if(response){
        this.role = response.role
        this.loggedIn = true
        this.notificationService.getNotifications();
        this.notificationService.notifications.subscribe(
          (value) => {
            this.numOfNotifications = value.length
          }
        )
      } else {
        this.loggedIn = false
      }
    })
  }


  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.route = this.router.url;
      }
    });
  }

  onLogout() {
    this.authService.logout()
  }

  openNotificationDialog() {
    let dialogRef = this.dialog.open(NotificationsComponent, {
      data: {
        notifications: this.notificationService.notifications.value
      }
    });
  }


}
