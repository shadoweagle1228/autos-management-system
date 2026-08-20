import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { AutoService } from '../../core/services/auto';
import { AuthService } from '../../core/services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-autos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './autos.html'
})
export class AutosComponent implements OnInit {
  autos: any[] = [];
  currentAuto: any = {};
  isEditing = false;
  
  // Filtros
  searchQuery = '';
  searchYear: number | null = null;
  searchBrand = '';
  errorMessage: string = '';

  constructor(
    private autoService: AutoService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadAutos();
  }

  loadAutos() {
    this.autoService.getAutos().subscribe(data => {
      this.autos = data;
      this.cdr.detectChanges();
    });
  }

  saveAuto(form: NgForm) {
    if (form.invalid) return;

    this.errorMessage = '';

    if (this.isEditing) {
      this.autoService.updateAuto(this.currentAuto.id, this.currentAuto).subscribe(() => {
        this.loadAutos();
        this.resetForm(form);
      });
    } else {
      this.autoService.createAuto(this.currentAuto).subscribe({
        next: () => {
          this.loadAutos();
          this.resetForm(form);
        },
        error: (err) => {
          this.errorMessage = '¡Error! Ya existe un auto registrado con esa placa.';
          this.cdr.detectChanges();
        }
      });
    }
  }

  editAuto(auto: any) {
    this.currentAuto = { ...auto };
    this.isEditing = true;
    this.cdr.detectChanges();
  }

  deleteAuto(id: number) {
    if(confirm('¿Estás seguro de eliminar este auto?')) {
      this.autoService.deleteAuto(id).subscribe(() => this.loadAutos());
    }
  }

  applyFilters() {
    this.autoService.searchAutos(this.searchQuery, this.searchYear || undefined, this.searchBrand)
      .subscribe(data => {
        this.autos = data;
        this.cdr.detectChanges();
      });
  }

  clearFilters() {
    this.searchQuery = '';
    this.searchYear = null;
    this.searchBrand = '';
    this.loadAutos();
  }

  resetForm(form?: NgForm) {
    this.currentAuto = {};
    this.isEditing = false;
    
    if (form) {
      form.resetForm(); 
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}