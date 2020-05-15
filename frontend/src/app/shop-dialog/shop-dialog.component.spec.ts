import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopDialogComponent } from './shop-dialog.component';

describe('ShopDialogComponent', () => {
  let component: ShopDialogComponent;
  let fixture: ComponentFixture<ShopDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
